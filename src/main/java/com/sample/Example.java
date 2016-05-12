package com.sample;

/**
 * This is a sample class to launch a rule.
 */
public class Example {



    public static class Message {

        public static final String HELLO = "Hello";
        public static final String GOODBYE = "GoodBye";

        private String message;

        private int age;

        public String getMessage() {
            return this.message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

}
