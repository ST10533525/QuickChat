public static void ensureFileExists() {
    try {
        File file = new File("login.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
    } catch (IOException e) {
        System.out.println("Error creating file: " + e.getMessage());
    }
}

public static void login() {
    ensureFileExists(); // make sure file exists before reading

    System.out.print("Enter your Username: ");
    String username = scanner.nextLine();
    System.out.print("Enter your password: ");
    String userpass = scanner.nextLine();

    boolean valid = false;
    try (BufferedReader br = new BufferedReader(new FileReader("login.txt"))) {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line.trim());
        }

        for (int i = 0; i < lines.size(); i += 3) {
            if (i + 1 < lines.size()) {
                String storedUser = lines.get(i);
                String storedPass = lines.get(i + 1);
                if (username.equals(storedUser) && userpass.equals(storedPass)) {
                    valid = true;
                    break;
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
    }

    if (valid) {
        System.out.println("Login successful!");
    } else {
        System.out.println("Invalid credentials");
    }
}

public static void register() {
    ensureFileExists(); // make sure file exists before appending

    System.out.print("Register your username: ");
    String userRegister = scanner.nextLine();

    if (!usernametest(userRegister)) return;

    System.out.print("Register password: ");
    String userpass = scanner.nextLine();

    if (!passkeytest(userpass)) return;

    String countryCode = "+27";
    System.out.print("Enter 10 digits (" + countryCode + "): ");
    String phoneNumber = scanner.nextLine();

    if (!phonetest(phoneNumber)) return;

    String fullPhone = countryCode + phoneNumber;

    try (FileWriter fw = new FileWriter("login.txt", true)) {
        fw.write(userRegister.trim() + "\n");
        fw.write(userpass.trim() + "\n");
        fw.write(fullPhone.trim() + "\n");
    } catch (IOException e) {
        System.out.println("Error writing file: " + e.getMessage());
    }

    System.out.println("You have successfully registered");
}