package org.task1;

public class Main {

    public static void main(String[] args) {

        Client client = new Client();
        client.startComponent(-1);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.endComponent();

    }
}
