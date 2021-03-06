package com.mojang.brigadier.tree;

import com.mojang.brigadier.AmbiguityConsumer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class CommandNode implements Comparable {

    private Map children = new LinkedHashMap();
    private Map literals = new LinkedHashMap();
    private Map arguments = new LinkedHashMap();
    private final Predicate requirement;
    private final CommandNode redirect;
    private final RedirectModifier modifier;
    private final boolean forks;
    private Command command;

    protected CommandNode(Command command, Predicate requirement, CommandNode redirect, RedirectModifier modifier, boolean forks) {
        this.command = command;
        this.requirement = requirement;
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }

    public Command getCommand() {
        return this.command;
    }

    public Collection getChildren() {
        return this.children.values();
    }

    public CommandNode getChild(String name) {
        return (CommandNode) this.children.get(name);
    }

    public CommandNode getRedirect() {
        return this.redirect;
    }

    public RedirectModifier getRedirectModifier() {
        return this.modifier;
    }

    public boolean canUse(Object source) {
        return this.requirement.test(source);
    }

    public void addChild(CommandNode node) {
        if (node instanceof RootCommandNode) {
            throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
        } else {
            CommandNode child = (CommandNode) this.children.get(node.getName());

            if (child != null) {
                if (node.getCommand() != null) {
                    child.command = node.getCommand();
                }

                Iterator iterator = node.getChildren().iterator();

                while (iterator.hasNext()) {
                    CommandNode grandchild = (CommandNode) iterator.next();

                    child.addChild(grandchild);
                }
            } else {
                this.children.put(node.getName(), node);
                if (node instanceof LiteralCommandNode) {
                    this.literals.put(node.getName(), (LiteralCommandNode) node);
                } else if (node instanceof ArgumentCommandNode) {
                    this.arguments.put(node.getName(), (ArgumentCommandNode) node);
                }
            }

            this.children = (Map) this.children.entrySet().stream().sorted(Entry.comparingByValue()).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> {
                return e1;
            }, LinkedHashMap::<init>));
        }
    }

    public void findAmbiguities(AmbiguityConsumer consumer) {
        HashSet matches = new HashSet();
        Iterator iterator = this.children.values().iterator();

        while (iterator.hasNext()) {
            CommandNode child = (CommandNode) iterator.next();
            Iterator iterator1 = this.children.values().iterator();

            while (iterator1.hasNext()) {
                CommandNode sibling = (CommandNode) iterator1.next();

                if (child != sibling) {
                    Iterator iterator2 = child.getExamples().iterator();

                    while (iterator2.hasNext()) {
                        String input = (String) iterator2.next();

                        if (sibling.isValidInput(input)) {
                            matches.add(input);
                        }
                    }

                    if (matches.size() > 0) {
                        consumer.ambiguous(this, child, sibling, matches);
                        matches = new HashSet();
                    }
                }
            }

            child.findAmbiguities(consumer);
        }

    }

    protected abstract boolean isValidInput(String s);

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof CommandNode)) {
            return false;
        } else {
            CommandNode that = (CommandNode) o;

            if (!this.children.equals(that.children)) {
                return false;
            } else {
                if (this.command != null) {
                    if (!this.command.equals(that.command)) {
                        return false;
                    }
                } else if (that.command != null) {
                    return false;
                }

                return true;
            }
        }
    }

    public int hashCode() {
        return 31 * this.children.hashCode() + (this.command != null ? this.command.hashCode() : 0);
    }

    public Predicate getRequirement() {
        return this.requirement;
    }

    public abstract String getName();

    public abstract String getUsageText();

    public abstract void parse(StringReader stringreader, CommandContextBuilder commandcontextbuilder) throws CommandSyntaxException;

    public abstract CompletableFuture listSuggestions(CommandContext commandcontext, SuggestionsBuilder suggestionsbuilder) throws CommandSyntaxException;

    public abstract ArgumentBuilder createBuilder();

    protected abstract String getSortedKey();

    public Collection getRelevantNodes(StringReader input) {
        if (this.literals.size() <= 0) {
            return this.arguments.values();
        } else {
            int cursor = input.getCursor();

            while (input.canRead() && input.peek() != 32) {
                input.skip();
            }

            String text = input.getString().substring(cursor, input.getCursor());

            input.setCursor(cursor);
            LiteralCommandNode literal = (LiteralCommandNode) this.literals.get(text);

            return (Collection) (literal != null ? Collections.singleton(literal) : this.arguments.values());
        }
    }

    public int compareTo(CommandNode o) {
        return this instanceof LiteralCommandNode == (o instanceof LiteralCommandNode) ? this.getSortedKey().compareTo(o.getSortedKey()) : (o instanceof LiteralCommandNode ? 1 : -1);
    }

    public boolean isFork() {
        return this.forks;
    }

    public abstract Collection getExamples();
}
