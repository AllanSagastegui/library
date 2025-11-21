package pe.ask.library.model.utils;

import java.util.List;

public class Pageable<T> {
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<T> content;

    public static <T> PageableBuilder<T> builder() {
        return new PageableBuilder<>();
    }

    public PageableBuilder<T> toBuilder() {
        return new PageableBuilder<T>().page(this.page).size(this.size).totalElements(this.totalElements).totalPages(this.totalPages).content(this.content);
    }

    public Pageable() {
    }

    public Pageable(int page, int size, long totalElements, int totalPages, List<T> content) {
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public List<T> getContent() {
        return this.content;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

}
