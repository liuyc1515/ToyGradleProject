package toy.learn;

import java.util.ArrayList;

public class GetClassName {
    private class GrandParent {
        GrandParent() {}

        String getClassName() {
            return this.getClass().getName();
        }

        String staticGetClassName() {
            return Thread.currentThread().getStackTrace()[1].getClassName();
        }

        String staticGetMethodName() {
            return Thread.currentThread().getStackTrace()[1].getMethodName();
        }
    }

    private class Parent extends GrandParent {
        Parent() {}
    }

    ArrayList<GrandParent> list;

    public GetClassName() {
        this.list = new ArrayList<GrandParent>();

        list.add(new Parent());
        list.add(new GrandParent());
    }

    public void run() {
        GrandParent grandparent = new GrandParent();
        Parent parent = new Parent();
        System.out.println(String.format("I am grand parent. My class name is %s", grandparent.getClassName()));
        System.out.println(String.format("I am grand parent. My class name is %s", grandparent.staticGetClassName()));
        System.out.println(String.format("I am grand parent. This method name is %s", grandparent.staticGetMethodName()));

        System.out.println(String.format("I am parent. My class name is %s", parent.getClassName()));
        System.out.println(String.format("I am parent. My class name is %s", parent.staticGetClassName()));
        System.out.println(String.format("I am parent. This method name is %s", parent.staticGetMethodName()));

        for (GrandParent item : list) {
            System.out.println(String.format("My class name is %s", item.getClassName()));
        }
    }
}
