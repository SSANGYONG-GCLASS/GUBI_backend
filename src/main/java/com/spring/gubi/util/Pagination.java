package com.spring.gubi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pagination {

    private int blockSize;      // 페이지 블럭 개수

    private int startPage;      // 시작 페이지 번호
    private int endPage;        // 끝 페이지 번호
    private int currentPage;    // 현재 페이지 번호
    private int totalPages;     // 총 페이지 수

    private boolean hasPrev;    // 이전 페이지 확인
    private boolean hasNext;    // 다음페이지 확인

    @Override
    public String toString() {
        return "Pagination{" +
                "startPage=" + startPage +
                ", endPage=" + endPage +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", blockSize=" + blockSize +
                ", hasPrev=" + hasPrev +
                ", hasNext=" + hasNext +
                '}';
    }
}
