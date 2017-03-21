import au.com.bytecode.opencsv.CSVReader;
import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**
 * Created by amy on 3/15/17.
 */

public class PredictPOJOTest {
    public static void main(String[] args) throws Exception {
      String inputCSVFileName = "/Users/amy/Desktop/mojovspojo/testdata.csv";
      String outputCSVFileName = "/Users/amy/Desktop/mojovspojo/testpred_mojo.csv";

      hex.genmodel.GenModel rawModel = new GBM_3000Trees();
      EasyPredictModelWrapper model = new EasyPredictModelWrapper(rawModel);
      System.out.print("Time in millis to score 200k: " + run(model, inputCSVFileName, outputCSVFileName));
//      System.out.print("\n");
//      System.out.print("Time in millis to score 200k: " + run(model, inputCSVFileName, outputCSVFileName));
//      System.out.print("\n");
//      System.out.print("Time in millis to score 200k: " + run(model, inputCSVFileName, outputCSVFileName));
    }

  private static long run(EasyPredictModelWrapper model, String inputCSVFileName, String outputCSVFileName) throws IOException, PredictException {
    CSVReader reader = new CSVReader(new FileReader(inputCSVFileName));
    BufferedWriter output = new BufferedWriter(new FileWriter(outputCSVFileName));

    long t1 = System.currentTimeMillis();
    RowData row = new RowData();
    String [] ary;
    String [] names = reader.readNext();
    while((ary = reader.readNext()) != null){
      for(int i = 0; i < ary.length; ++i) {
        row.put(names[i], ary[i].isEmpty()? Double.toString(Double.NaN): ary[i]);
      }
      BinomialModelPrediction p = model.predictBinomial(row);
      output.write(p.label);
      output.write(",");
      for (int i = 0; i < p.classProbabilities.length; i++) {
        if (i > 0) {
          output.write(",");
        }
        output.write(Double.toString(p.classProbabilities[i]));
      }
      output.write("\n");
      row.clear();
    }
    output.close();
    long t2 = System.currentTimeMillis();
    long elapsed = t2 - t1;
    return(elapsed);
  }


}

