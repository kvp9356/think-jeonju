package com.kvp.thinkjeonju.support;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private static final int pageCount = 5;
    private static final int contentCount = 8;
    private int currentBlockStart;
    private int currentBlockEnd;
    private int currentBlock;
    private int blockStart;
    private int blockEnd;
    private int lastPage;

    public void makeCurrentBlock(int currentPage) {
        currentBlockStart = (currentPage - 1) * contentCount + 1;
        currentBlockEnd = currentBlockStart + contentCount - 1;
    }

    public void makeBlock(int currentPage) {
        int block = (int)Math.floor((currentPage - 1) / pageCount);
        currentBlock = currentPage;
        blockStart = pageCount * block + 1;
        blockEnd = blockStart + pageCount - 1;

        if(blockEnd > lastPage)
            blockEnd = lastPage;
    }

    public void makeLastPage(int total) {
        if(total % contentCount == 0) {
            lastPage = (int)Math.floor(total / contentCount);
        } else {
            lastPage = (int)Math.floor(total/contentCount) + 1;
        }
    }
}
