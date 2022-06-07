package com.boot.jpa.multidatasource.repository.book;

import com.boot.jpa.multidatasource.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

}
