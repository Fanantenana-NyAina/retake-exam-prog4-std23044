package com.retake.exam.file.hash;

import com.retake.exam.PojaGenerated;

@PojaGenerated
public record FileHash(FileHashAlgorithm algorithm, String value) {}
