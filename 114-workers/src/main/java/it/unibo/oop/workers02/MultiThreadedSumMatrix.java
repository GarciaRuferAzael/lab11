package it.unibo.oop.workers02;

import java.util.stream.IntStream;

public class MultiThreadedSumMatrix implements SumMatrix {
    
    private final int nthread;

    public MultiThreadedSumMatrix(final int nthread){
        super();
        if(nthread < 1){
            throw new IllegalArgumentException();
        }
        this.nthread = nthread;
    }

    @Override
    public double sum(final double[][] matrix) {
        final int size = matrix.length / nthread + matrix.length % nthread;

        return IntStream
            .iterate(0, start -> start + size)
            .limit(nthread)
            .parallel()
            .mapToDouble(start -> {
                double res = 0;
                for (int i = start; i < matrix.length && i < start + size; i++) {
                    for (final double d : matrix[i]) {
                        res += d;
                    }
                }
                return res;
            })
            .sum();
    }
    
}
