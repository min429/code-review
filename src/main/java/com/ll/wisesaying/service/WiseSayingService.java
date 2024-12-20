package com.ll.wisesaying.service;

import static com.ll.wisesaying.constant.Constant.*;
import static com.ll.wisesaying.util.InputUtil.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.ll.wisesaying.domain.Search;
import com.ll.wisesaying.domain.WiseSaying;
import com.ll.wisesaying.repository.WiseSayingRepository;

public class WiseSayingService {

    private final WiseSayingRepository wiseSayingRepository;

    public WiseSayingService(WiseSayingRepository wiseSayingRepository) {
        this.wiseSayingRepository = wiseSayingRepository;
    }

    public WiseSaying inputWiseSaying(String content, String author) {
        int idx = wiseSayingRepository.getNextIdx();

        WiseSaying wiseSaying = new WiseSaying(idx, content, author);
        wiseSayingRepository.getDB().put(idx, wiseSaying);

        return wiseSaying;
    }

    public List<WiseSaying> getWiseSayings() {
        return wiseSayingRepository.getSubWiseSayings(new ArrayList<>(wiseSayingRepository.getDB().values()), 1L);
    }

    public List<WiseSaying> getWiseSayings(Search search) {
        return wiseSayingRepository.findWiseSayings(search);
    }

    public Integer deleteWiseSaying(String cmd) {
        int idx = getCmdIdx(cmd);
        wiseSayingRepository.containsWiseSaying(idx);

        wiseSayingRepository.getDB().remove(idx);

        return idx;
    }

    public WiseSaying editWiseSaying(int idx, String content, String author) {
        WiseSaying newWiseSaying = new WiseSaying(idx, content, author);
        wiseSayingRepository.getDB().put(idx, newWiseSaying);

        return newWiseSaying;
    }

    public WiseSaying getWiseSaying(String cmd) {
        int idx = getCmdIdx(cmd);
        wiseSayingRepository.containsWiseSaying(idx);

        return wiseSayingRepository.getDB().get(idx);
    }

    public void buildPhrase() throws IOException {
        wiseSayingRepository.saveAllWiseSayings(new ArrayList<>(wiseSayingRepository.getDB().values()));
    }

    public void saveFile(WiseSaying wiseSaying) throws IOException {
        wiseSayingRepository.saveWiseSaying(wiseSaying);
        wiseSayingRepository.saveLastIdx(wiseSaying.getId());
    }

    public void deleteFile(int idx) throws IOException {
        Files.delete(getPath(idx));
        wiseSayingRepository.saveLastIdx(--idx);
    }
}
