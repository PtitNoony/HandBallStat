/*
 * Copyright (C) 2015 HAMON-KEROMEN A.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.noony.handstats.utils;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Arnaud HAMON
 */
public class MathSolver {

    private final List<Integer> values;

    private final int targetValue;
    private final int nbNumbers;

    public MathSolver() {
        values = new LinkedList<>();
        values.add(1);
        values.add(3);
        values.add(5);
        values.add(7);
        values.add(9);
        values.add(11);
        values.add(13);
        values.add(15);
        targetValue = 30;
        nbNumbers = 3;
    }

    private void solve() {
        int result;
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size(); j++) {
                for (int k = 0; k < values.size(); k++) {
                    result = values.get(i) + values.get(j) + values.get(k);
                    if (result == targetValue) {
                        System.err.println(" " + values.get(i) + "+" + values.get(j) + "+" + values.get(k) + "=" + result);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        MathSolver solver = new MathSolver();
        solver.solve();
    }

}
