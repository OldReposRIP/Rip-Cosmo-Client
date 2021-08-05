package cope.cosmos.client.features;

public class Feature {

    public String name;
    public String description;

    public Feature(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
