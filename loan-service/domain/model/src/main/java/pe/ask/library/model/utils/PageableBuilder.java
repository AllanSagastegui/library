package pe.ask.library.model.utils;

import java.util.List;

public class PageableBuilder<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> content;

    PageableBuilder() {
    }

    public PageableBuilder<T> page(int page) {
        this.page = page;
        return this;
    }

    public PageableBuilder<T> size(int size) {
        this.size = size;
        return this;
    }

    public PageableBuilder<T> totalElements(long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public PageableBuilder<T> totalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public PageableBuilder<T> content(List<T> content) {
        this.content = content;
        return this;
    }

    public Pageable<T> build() {
        return new Pageable<>(page, size, totalElements, totalPages, content);
    }
}
