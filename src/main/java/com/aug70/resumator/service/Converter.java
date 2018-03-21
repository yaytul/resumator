package com.aug70.resumator.service;

import java.io.IOException;

import com.aug70.resumator.model.ConvertedFile;
import com.aug70.resumator.model.UploadedFile;

interface Converter {

	ConvertedFile[] convert(UploadedFile file) throws IOException ;

}
