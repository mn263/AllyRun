package com.modelUtils;

import java.util.*;

public class Matrix4D {

	private List<List<Double>> matrix = new ArrayList<>();

	public Matrix4D(ArrayList<Double> cells) {
		matrix.add(createRow(cells.get(0), cells.get(1), cells.get(2), cells.get(3)));
		matrix.add(createRow(cells.get(4), cells.get(5), cells.get(6), cells.get(7)));
		matrix.add(createRow(cells.get(8), cells.get(9), cells.get(10), cells.get(11)));
		matrix.add(createRow(cells.get(12), cells.get(13), cells.get(14), cells.get(15)));
	}

	public Matrix4D(double _11, double _12, double _13, double _14,
					double _21, double _22, double _23, double _24,
					double _31, double _32, double _33, double _34,
					double _41, double _42, double _43, double _44) {

		matrix.add(createRow(_11, _12, _13, _14));
		matrix.add(createRow(_21, _22, _23, _24));
		matrix.add(createRow(_31, _32, _33, _34));
		matrix.add(createRow(_41, _42, _43, _44));
	}

	private List<Double> createRow(double v, double v1, double v2, double v3) {
		List<Double> list = new ArrayList<>();
		list.add(v);
		list.add(v1);
		list.add(v2);
		list.add(v3);
		return list;
	}

	public List<Double> getRow(int row) {
		return matrix.get(row);
	}

	public void setRow(int row, ArrayList<Double> list) {
		matrix.set(row, list);
	}
}
