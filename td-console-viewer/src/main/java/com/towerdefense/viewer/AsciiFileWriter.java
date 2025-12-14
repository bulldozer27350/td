package com.towerdefense.viewer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AsciiFileWriter {

	public static void write(String path, String content, int tick) {
		try {
			Files.write(
					Paths.get(path), 
					("Nombre de tours de jeu : " + tick + "\n" + content).getBytes());
		} catch (IOException e) {
			throw new RuntimeException("Erreur écriture fichier viewer", e);
		}
	}
}
