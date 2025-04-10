package org.springlibrary.services;

import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class InputService {
    private final Scanner scanner;

    public InputService() {
        this.scanner = new Scanner(System.in);
    }

    public String prompt(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();
        System.out.println();
        return input;
    }
}
