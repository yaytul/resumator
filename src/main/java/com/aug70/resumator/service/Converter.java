package com.aug70.resumator.service;

import java.util.stream.Stream;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

@FunctionalInterface
interface Converter {

	Stream<ConvertedFile> convert(UploadedFile file);

}
