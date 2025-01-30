package com.polarbookshop.catalogservice.demo;

import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;

@Component
@Profile("testdata")
public class BootDataLoader {
    private final BookRepository bookRepository;

    public BootDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {
        bookRepository.deleteAll();

        var book1 = Book.of("9791158395599", "Vue3와 스프링 부트로 시작하는 웹 개발 철저 입문", "최진", 26.2);
        var book2 = Book.of("1835084230", "Solutions Architect's Handbook - Third Edition: Kick-start your career with architecture design principles, strategies, and generative AI (English Edition)", "Saurabh Shrivastava", 59.99);
        var book3 = Book.of("180323895X", "AWS for Solutions Architects - Second Edition: The definitive guide to AWS Solutions Architecture for migrating to, building, scaling, and succeeding in the cloud (English Edition)", "Saurabh Shrivastava", 54.99);

        bookRepository.saveAll(List.of(book1, book2, book3));
    }
}

