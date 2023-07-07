package toy.learn;

import java.util.ArrayList;

public class ForeachConsumer {

    private abstract class GrandParent {

        Integer id;

        public GrandParent(Integer id) {
            this.id = id;
        }

        abstract public void onIteration();
    }

    private class Parent extends GrandParent {

        public Parent(Integer id) {
            super(id);
        }

        public void onIteration() {
            System.out.println(String.format("Item %d is %s", this.id, "parent"));
        }
    }

    private class Child extends Parent {

        public Child(Integer id) {
            super(id);
        }

        public void onIteration() {
            System.out.println(String.format("Item %d is %s", this.id, "child"));
        }
    }

    ArrayList<GrandParent> list;

    public ForeachConsumer() {
        this.list = new ArrayList<GrandParent>();

        list.add(new Parent(1));
        list.add(new Child(2));
        list.add(new Parent(3));
        list.add(new Child(4));
        list.add(new GrandParent(5) {
            public void onIteration() {
                System.out.println(String.format("Item %d is %s", this.id, "stranger"));
            }
        });
    }

    public void run() {
        list.forEach(GrandParent::onIteration);
    }
}
