package com.hy.java.utility.math.algebra;

import java.math.BigDecimal;

public class Vector {
	private int dimension;
	private BigDecimal[] coordinates;

	private enum Operations {
		POSITIVE, GET_SET, VECTOR_ADDITION, DOT_PRODUCT
	}

	public Vector(int dimension) {
		if (checkDimension(dimension, 1, Operations.POSITIVE)) {
			this.dimension = dimension;
			coordinates = new BigDecimal[this.dimension];
		}
	}

	public int getDimension() {
		return dimension;
	}

	public void setCoordinate(int coordinate_index, double coordinate) {
		if (checkDimension(coordinate_index, getDimension(), Operations.GET_SET)) {
			coordinates[coordinate_index - 1] = BigDecimal.valueOf(coordinate);
		}
	}

	public void setCoordinate(int coordinate_index, BigDecimal coordinate) {
		if (checkDimension(coordinate_index, getDimension(), Operations.GET_SET)) {
			coordinates[coordinate_index - 1] = coordinate;
		}
	}

	public BigDecimal getCoordinate(int coordinate_index) {
		BigDecimal result = null;
		if (checkDimension(coordinate_index, getDimension(), Operations.GET_SET)) {
			result = coordinates[coordinate_index - 1];
		}
		return result;
	}

	public BigDecimal[] getCoordinates() {
		return coordinates;
	}

	/**
	 * 向量加法
	 * 
	 * @param vector_A
	 * @param vector_B
	 * @return vector_A+vector_B
	 */
	public static Vector vectorAddition(Vector vector_A, Vector vector_B) {
		Vector result = null;
		// 同型则可以做加法
		if (checkDimension(vector_A.getDimension(), vector_B.getDimension(), Operations.VECTOR_ADDITION)) {
			result = new Vector(vector_A.getDimension());
			for (int coordinate_index = 1; coordinate_index <= result.getDimension(); coordinate_index++) {
				result.setCoordinate(coordinate_index, vector_A.getCoordinate(coordinate_index).add(vector_B.getCoordinate(coordinate_index)));
			}
		}
		return result;
	}

	/**
	 * 向量减法
	 * 
	 * @param vector_A
	 * @param vector_B
	 * @return vector_A-vector_B
	 */
	public static Vector vectorSubtraction(Vector vector_A, Vector vector_B) {
		return Vector.vectorAddition(vector_A, Vector.scalarMultiplication(BigDecimal.valueOf(-1.0), vector_B));
	}

	/**
	 * 向量数乘
	 * 
	 * @param num
	 * @param vector
	 * @return num*vector
	 */
	public static Vector scalarMultiplication(BigDecimal num, Vector vector) {
		Vector result = null;
		if (checkDimension(vector.getDimension(), 1, Operations.POSITIVE)) {
			result = new Vector(vector.getDimension());
			for (int coordinate_index = 1; coordinate_index <= result.getDimension(); coordinate_index++) {
				result.setCoordinate(coordinate_index, vector.getCoordinate(coordinate_index).multiply(num));
			}
		}
		return result;
	}

	/**
	 * 向量点乘
	 * 
	 * @param vector_A
	 * @param vector_B
	 * @return vector_A·vector_B
	 */
	public static BigDecimal dotProduct(Vector vector_A, Vector vector_B) {
		BigDecimal result = new BigDecimal("0");
		// 同型则可以做点乘
		if (checkDimension(vector_A.getDimension(), vector_B.getDimension(), Operations.DOT_PRODUCT)) {
			for (int coordinate_index = 1; coordinate_index <= vector_A.getDimension(); coordinate_index++) {
				result = result.add(vector_A.getCoordinate(coordinate_index).multiply(vector_B.getCoordinate(coordinate_index)));
			}
		}
		return result;
	}

	public static BigDecimal norm(Vector vector) {
		BigDecimal result = null;
		if (checkDimension(vector.getDimension(), 1, Operations.POSITIVE)) {
			result = BigDecimal.valueOf(Math.sqrt(Vector.dotProduct(vector, vector).doubleValue()));
		}
		return result;
	}

	/**
	 * 根据操作类型，检查操作涉及的长度是否满足操作要求
	 * 
	 * @param dimension_A
	 * @param dimension_B
	 * @param mode
	 * @return dimension_ok
	 */
	private static boolean checkDimension(int dimension_A, int dimension_B, Operations mode) {
		boolean dimension_ok = false;
		boolean dimension_A_ok = false;
		boolean dimension_B_ok = false;
		if (dimension_A > 0) {
			dimension_A_ok = true;
		} else {
			System.out.println("dimension_A must be greater than 0");
		}
		if (dimension_B > 0) {
			dimension_B_ok = true;
		} else {
			System.out.println("dimension_B must be greater than 0");
		}
		if (dimension_A_ok && dimension_B_ok) {
			switch (mode) {
			case POSITIVE: {
				dimension_ok = true;
				break;
			}
			// 向量坐标操作：检查操作坐标是否在向量内
			case GET_SET: {
				if (dimension_A > 0 && dimension_A <= dimension_B) {
					dimension_ok = true;
				} else {
					System.out.println("dimension out of bounds");
				}
				break;
			}
			// 向量加法、点乘：检查两个向量是否同型
			case VECTOR_ADDITION:
			case DOT_PRODUCT: {
				if (dimension_A == dimension_B) {
					dimension_ok = true;
				} else {
					System.out.println("dimension_A not equals with dimension_B");
				}
				break;
			}
			default: {
				System.err.println("向量操作模式出错");
				break;
			}
			}
		}
		return dimension_ok;
	}

	/**
	 * 打印一个向量，同时会将打印内容以String形式返回
	 * 
	 * @param vector
	 * @return {@code String}格式的vector内容
	 */
	public static String print(Vector vector) {
		String result = null;
		StringBuilder sb = new StringBuilder();
		// 先向sb中添加第一个元素
		int coordinate_index = 1;
		sb.append(vector.getCoordinate(coordinate_index).doubleValue());
		coordinate_index++;
		// 后面的元素前都加制表符
		while (coordinate_index <= vector.getDimension()) {
			sb.append("\t" + vector.getCoordinate(coordinate_index).doubleValue());
			coordinate_index++;
		}
		result = sb.toString();
		System.out.println(result);
		return result;
	}
}
