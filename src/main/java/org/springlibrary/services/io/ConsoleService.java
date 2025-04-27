package org.springlibrary.services.io;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class ConsoleService extends IOService {
    private final Scanner scanner;

    public ConsoleService() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String prompt(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();
        System.out.println();
        return input;
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
