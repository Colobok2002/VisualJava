package table;

public class Person {
    
        private int id;
        private String firstName;
        private String lastName;
        private String phone;
        private String email;

        public Person(int id, String firstName, String lastName, String phone, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.email = email;
        }

        public int getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }
    }