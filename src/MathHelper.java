public class MathHelper {
    public Double[][] mult_matrix(Double[][] a, Double[][] b){
        Double[][] result = new Double[a.length][a[0].length];

        for (int i = 0; i < a.length; i++){
            for (int j = 0; j < b[0].length; j++){
                result[i][j] = 0.0;
                for(int k = 0; k < a[0].length; k++){
                    result[i][j] += a[i][k]*b[k][j];
                }
            }
        }
        return result;
    }
    public double euclidean_dist(Double[][] a, Double[][] b){
        double result = 0f;

        for (int i = 0; i < a[0].length; i++) {
            result += (a[0][i]-b[0][i])*(a[0][i]-b[0][i]); //basically just (a+b)^2 but im too lazy to check the exp function in java
        }
        return Math.sqrt(result);
    }

}
