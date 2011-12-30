/*
This file is part of jpcsp.

Jpcsp is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Jpcsp is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */
package jpcsp.graphics.RE.software;

import static jpcsp.graphics.RE.software.PixelColor.getColor;
import jpcsp.graphics.VertexState;

/**
 * @author gid15
 *
 */
public class VertexSourceReader {
	public static ISourceReader getVertexSourceReader(VertexState v1, VertexState v2, VertexState v3) {
		ISourceReader reader;

		if (sameColor(v1, v2, v3)) {
			reader = new ColorSourceReader(v1.c);
		} else if (v3 != null) {
			reader = new VertexTriangleSourceReader(v1, v2, v3);
		} else {
			reader = new VertexSpriteSourceReader(v1, v2);
		}

		return reader;
	}

	private static boolean sameColor(VertexState v1, VertexState v2) {
		if (v2 != null) {
			for (int i = 0; i < v1.c.length; i++) {
				if (v1.c[i] != v2.c[i]) {
					return false;
				}
			}
		}

		return true;
	}

	private static boolean sameColor(VertexState v1, VertexState v2, VertexState v3) {
		if (v3 == null) {
			return sameColor(v1, v2);
		}

		for (int i = 0; i < v1.c.length; i++) {
			if (v1.c[i] != v2.c[i] || v1.c[i] != v3.c[i]) {
				return false;
			}
		}

		return true;
	}

	private static final class VertexTriangleSourceReader implements ISourceReader {
		private final int[] color1 = new int[4];
		private final int[] color2 = new int[4];
		private final int[] color3 = new int[4];

		public VertexTriangleSourceReader(VertexState v1, VertexState v2, VertexState v3) {
			for (int i = 0; i < color1.length; i++) {
				color1[i] = getColor(v1.c[i]);
				color2[i] = getColor(v2.c[i]);
				color3[i] = getColor(v3.c[i]);
			}
		}

		@Override
		public int read(PixelState pixel) {
			int a = pixel.getTriangleWeightedValue(color1[3], color2[3], color3[3]);
			int b = pixel.getTriangleWeightedValue(color1[2], color2[2], color3[2]);
			int g = pixel.getTriangleWeightedValue(color1[1], color2[1], color3[1]);
			int r = pixel.getTriangleWeightedValue(color1[0], color2[0], color3[0]);

			return getColor(a, b, g, r);
		}
	}

	private static final class VertexSpriteSourceReader implements ISourceReader {
		private final int[] color1 = new int[4];
		private final int[] color2 = new int[4];

		public VertexSpriteSourceReader(VertexState v1, VertexState v2) {
			for (int i = 0; i < color1.length; i++) {
				color1[i] = getColor(v1.c[i]);
				color2[i] = getColor(v2.c[i]);
			}
		}

		@Override
		public int read(PixelState pixel) {
			// TODO Interpolate between color1 and color2
			return getColor(color1);
		}
	}
}