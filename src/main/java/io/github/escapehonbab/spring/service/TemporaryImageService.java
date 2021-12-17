package io.github.escapehonbab.spring.service;

import kotlin.Pair;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class TemporaryImageService {

    private final Set<Pair<String, byte[]>> hashes = new HashSet<>();

    public String generateHash(byte[] image) {
        String hash = RandomStringUtils.randomAlphabetic(7);
        hashes.add(new Pair<>(hash, image));
        return hash;
    }

    public Optional<byte[]> getImage(String hash) {
        Optional<Pair<String, byte[]>> opt = hashes.stream().filter(h -> h.getFirst().equals(hash)).findAny();
        if (opt.isPresent()) {
            hashes.remove(opt.get());
            return Optional.ofNullable(opt.get().getSecond());
        } else {
            return Optional.ofNullable(null);
        }
    }
}
