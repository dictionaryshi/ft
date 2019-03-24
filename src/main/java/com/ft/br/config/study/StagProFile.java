package com.ft.br.config.study;

/**
 * 测试环境
 *
 * @author shichunyang
 */
public class StagProFile implements ProFile {
	@Override
	public String proFile() {
		return "stag环境";
	}
}
