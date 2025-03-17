package com.bridgelabz.fileio;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Contact{
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String email;

    public Contact(String firstName, String lastName, String address, String city, String state, String zipCode, String phoneNumber, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    //Getter
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    //Setter
    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Override equals() to prevent duplicates based on first and last name
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return firstName.equalsIgnoreCase(contact.firstName) && lastName.equalsIgnoreCase(contact.lastName);
    }

    // Override hashCode() to match equals()
    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase(), lastName.toLowerCase());
    }

    public String toString() {
        return firstName + " " + lastName + " | " + city + ", " + state + " | " + phoneNumber + " | " + email;
    }

}

class AddressBook{
    static List<Contact> contactArrayList = new ArrayList<>();
    private static final String FILENAME = "src/com/bridgelabz/fileio/SampleInput.txt";

    public boolean addContact(Contact contact) {
        boolean isDuplicate = contactArrayList.stream().anyMatch(existingUser -> existingUser.equals(contact)); // Stream API for duplicate check

        if (isDuplicate) {
            System.out.println("Duplicate Entry ");
            return false;
        }

        contactArrayList.add(contact);
        return true;
    }

    public void editContact(String fName, String lName, Scanner scanner){
        for(Contact c : contactArrayList){
            if(c.getFirstName().equals(fName) && c.getLastName().equals(lName)){
                System.out.print("Enter new number: ");
                String newNumber = scanner.nextLine();
                c.setPhoneNumber(newNumber);
                System.out.println("Contact updated successfully");
                break;
            }
        }

    }

    public void removeContact(String fName, String lName){
        for(Contact c : contactArrayList){
            if(c.getFirstName().equals(fName) && c.getLastName().equals(lName)){
                contactArrayList.remove(c);
            }
        }
    }

    public List<Contact> getUsers(){
        return contactArrayList;
    }


    //UC11
    public List<Contact> sortContactsByName() {
        return contactArrayList.stream()
                .sorted(Comparator.comparing(Contact::getFirstName)
                        .thenComparing(contact -> contact.getLastName()))  // Sort by First Name, then Last Name
                .collect(Collectors.toList());

    }

    //UC12
    public List<Contact> sortContactsByCity(){
        return contactArrayList.stream()
                .sorted(Comparator.comparing(Contact::getCity)).collect(Collectors.toList());
    }
    public List<Contact> sortContactsByZip(){
        return contactArrayList.stream()
                .sorted(Comparator.comparing(Contact::getZipCode)).collect(Collectors.toList());
    }
    public List<Contact> sortContactsByState(){
        return contactArrayList.stream()
                .sorted(Comparator.comparing(Contact::getState)).collect(Collectors.toList());
    }


    // Write contacts to a text file
    public void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            for (Contact contact : contactArrayList) {
                writer.write(contact.getFirstName() + "," + contact.getLastName() + "," + contact.getAddress() + "," + contact.getCity() + "," + contact.getState() + "," + contact.getZipCode() + "," + contact.getPhoneNumber() + "," + contact.getEmail());
                writer.newLine(); // New line for each contact
            }
            System.out.println("Contacts saved successfully to " + FILENAME);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    // Read contacts from a text file
    public void readFromFile() {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("No previous records found. Starting fresh.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            contactArrayList.clear(); // Clear existing list before loading from file

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 8) { // Ensure all fields are present
                    Contact contact = new Contact(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
                    contactArrayList.add(contact);
                }
            }
            System.out.println("Contacts loaded successfully from " + FILENAME);
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }


}

class Systems{
    Map<String, AddressBook> addressBookMap = new HashMap<>();

    public void addAddressBook(String addressBookName, AddressBook newAddressBook){
        if(addressBookMap.containsKey(addressBookName)){
            System.out.println("Already Exists");
        }
        else addressBookMap.put(addressBookName, newAddressBook);
    }

    //UC8
    public List<Contact> searchByCity(String city){
        return addressBookMap.values().stream().flatMap(ab->ab.getUsers().stream())
                .filter(contact->contact.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }
    public List<Contact> searchByState(String state){
        return addressBookMap.values().stream().flatMap(ab->ab.getUsers().stream())
                .filter(contact->contact.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

    //UC9
    public Map<String, List<Contact>> getUserByCity() {
        return addressBookMap.values().stream().flatMap(ab -> ab.getUsers().stream())
                .collect(Collectors.groupingBy(contact -> contact.getCity()));

    }
    public Map<String, List<Contact>> getUserByState() {
        return addressBookMap.values().stream().flatMap(ab -> ab.getUsers().stream())
                .collect(Collectors.groupingBy(Contact::getCity));

    }

    //UC10
    public Map<String, Long> getUserCountByCity(){
        return addressBookMap.values().stream().flatMap(ab->ab.getUsers().stream())
                .collect(Collectors.groupingBy(contact -> contact.getCity(), Collectors.counting()));
    }

    public Map<String, Long> getUserCountByState(){
        return addressBookMap.values().stream().flatMap(ab->ab.getUsers().stream())
                .collect(Collectors.groupingBy(contact -> contact.getState(), Collectors.counting()));
    }


}

public class AddressBookMain {
    public static void main(String[] args) {


        System.out.println("Welcome to Address Book program");


        AddressBook addressBook = new AddressBook();

        addressBook.readFromFile();
            // Adding contacts
        addressBook.addContact(new Contact("Mayank", "Pal", "123 Street", "New York", "NY", "10001", "987654321", "mayank@example.com"));
        addressBook.addContact(new Contact("Vibhor", "Gupta", "456 Road", "Los Angeles", "CA", "90001", "987654322", "vibhor@example.com"));
        addressBook.addContact(new Contact("Kanika", "Agarwal", "789 Street", "Chicago", "IL", "60601", "987654323", "kanika@example.com"));
        addressBook.addContact(new Contact("Arnav", "Saharan", "101 Ny", "New York", "NY", "10002", "987654324", "arnav@example.com"));

        addressBook.writeToFile();
        // Display all contacts
        System.out.println("\n All Contacts:");
        addressBook.getUsers().forEach(System.out::println);

        // Sorting by Name
        System.out.println("\n Sorted by Name:");
        addressBook.sortContactsByName().forEach(System.out::println);

        // Sorting by City
        System.out.println("\n Sorted by City:");
        addressBook.sortContactsByCity().forEach(System.out::println);

        // Sorting by State
        System.out.println("\n Sorted by State:");
        addressBook.sortContactsByState().forEach(System.out::println);


        // Edit an existing contact
        Scanner scanner = new Scanner(System.in);

        addressBook.editContact("Vibhor", "Gupta", scanner);


        // Display updated contacts
        System.out.println("\n Contacts after Editing & Deleting:");
        addressBook.getUsers().forEach(System.out::println);

        // Create an AddressBook System with multiple address books
        Systems system = new Systems();
        system.addAddressBook("Personal", addressBook);

        // Searching users by City
        System.out.println("\n Users in New York:");
        system.searchByCity("New York").forEach(System.out::println);

        // Searching users by State
        System.out.println("\n Users in California:");
        system.searchByState("CA").forEach(System.out::println);

        // Counting users by City
        System.out.println("\n Count of users in each City:");
        system.getUserCountByCity().forEach((city, count) -> System.out.println(city + " = " + count));

        // Counting users by State
        System.out.println("\n Count of users in each State:");
        system.getUserCountByState().forEach((state, count) -> System.out.println(state + " = " + count));


    }
}
