/**
 * (C) Copyright 2024 Araf Karsh Hamid
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fusion.air.microservice.ai.ml.supervised.classification;

import io.fusion.air.microservice.utils.Std;
import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import java.io.IOException;

/**
 * A Simple Multi Layered Perceptron (MLP) applied to digit classification for
 * the MNIST Dataset (http://yann.lecun.com/exdb/mnist/).
 *
 * This file builds one input layer and one hidden layer.
 * The input layer has input dimension of numRows*numColumns where these variables indicate the
 * number of vertical and horizontal pixels in the image. This layer uses a rectified linear unit
 * (relu) activation function. The weights for this layer are initialized by using Xavier initialization
 * (https://prateekvjoshi.com/2016/03/29/understanding-xavier-initialization-in-deep-neural-networks/)
 * to avoid having a steep learning curve. This layer will have 1000 output signals to the hidden layer.
 *
 * The hidden layer has input dimensions of 1000. These are fed from the input layer. The weights
 * for this layer is also initialized using Xavier initialization. The activation function for this
 * layer is a softmax, which normalizes all the 10 outputs such that the normalized sums
 * add up to 1. The highest of these normalized values is picked as the predicted class.
 *
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class _02_Multi_Layered_Perceptron_Example {

    /**
     * Handwritten Digit 0 -9 Image recognition.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // The MNIST (Modified National Institute of Standards and Technology) dataset is a well-known
        // benchmark dataset in the field of machine learning and computer vision. It is commonly used for
        // training and testing various machine learning models, particularly in the area of image classification.
        // Here are the key details about the MNIST dataset:
        //
        //	•	Content: The dataset consists of 70,000 grayscale images of handwritten digits, from 0 to 9.
        //	•	Training Set: 60,000 images
        //	•	Test Set: 10,000 images
        //	•	Image Size: Each image is a 28x28 pixel square, making for a total of 784 pixels per image.
        //	•	Grayscale: Each pixel is represented by an integer value between 0 (black) and 255 (white).
        // Step 1:
        Std.println("Step 1: Create DataSetIterators For Training and Test Data... ");
        int batchSize = 128;    // batch size for each epoch
        int rngSeed = 123;      // random number seed for reproducibility
        //Get the DataSetIterators:
        DataSetIterator mnistTrain = createDataSetIterator(batchSize, true, rngSeed);
        DataSetIterator mnistTest  = createDataSetIterator(batchSize, false, rngSeed);
        // Step 2: Create the Neural Network Configuration
        MultiLayerConfiguration conf = createMultiModelConfig( rngSeed);
        // Step 3: Create the Model
        // Step 4: Run the Model on mnistTrain Data Set
        MultiLayerNetwork model = createAndRunMultiLayerNetworkModel(conf,  mnistTrain,  250);
        // Step 5: Evaluate the Model
        evaluateModel( model,  mnistTest);
    }

    /**
     * Create the Data Set Iterator
     *
     * @param batchSize
     * @param train
     * @param rngSeed
     * @return
     * @throws IOException
     */
    private static MnistDataSetIterator createDataSetIterator(
            int batchSize, boolean train, int rngSeed) throws IOException {
        return new MnistDataSetIterator(batchSize, train, rngSeed);
    }

    /**
     * Create the Multi Layer Configuration for the Neural Network
     * This configuration defines a simple two-layer neural network:
     * •	Input Layer: A dense layer with 784 input neurons (one for each pixel in the MNIST images),
     *      1000 output neurons, ReLU activation, and Xavier weight initialization.
     * •	Output Layer: An output layer with 1000 input neurons (from the previous layer), 10 output
     *      neurons (one for each digit), softmax activation for probability output, and Xavier weight initialization.
     * @param rngSeed
     * @return
     */
    private static MultiLayerConfiguration createMultiModelConfig(int rngSeed) {
        Std.println("Step 2: Create Multi Layer Configuration... ");
        // To Create 784 Neuron Input layer
        final int numRows = 28;
        final int numColumns = 28;
        // Number of output classes - Handwritten Digits from 0 - 9
        int outputNum = 10;
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                // The seed method sets a random seed to ensure reproducibility of the results. Using
                // the same seed will produce the same random numbers across different runs.
                .seed(rngSeed)
                // The updater method specifies the optimization algorithm. Here, it uses
                // Nesterov’s Accelerated Gradient with a learning rate of 0.006 and a momentum of 0.9.
                .updater(new Nesterovs(0.006, 0.9))
                // This adds L2 regularization with a coefficient of 1 \times 10^{-4} to prevent
                // overfitting by penalizing large weights.
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder()          // Create the first, input layer with xavier initialization
                        //	nIn(numRows * numColumns): Sets the number of input neurons.
                        .nIn(numRows * numColumns)      //	For MNIST, this is 28x28 = 784 (each pixel in the 28x28 image is an input).
                        .nOut(1000)                          // nOut(1000): Sets the number of output neurons in this layer to 1000.
                        .activation(Activation.RELU)       //	Uses the ReLU activation function for the neurons in this layer.
                        .weightInit(WeightInit.XAVIER)  //	Initializes the weights using Xavier initialization, which is good for layers with ReLU activation.
                        .build())
                // Starts the configuration of the output layer with the negative log likelihood loss function, which is suitable for classification tasks.
                .layer(new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
                        .nIn(1000)                           // Sets the number of input neurons to 1000 (output from the previous dense layer).
                        .nOut(outputNum)                  // Sets the number of output neurons, typically 10 for MNIST (one for each digit).
                        .activation(Activation.SOFTMAX) // Uses the softmax activation function to convert the output to probabilities.
                        .weightInit(WeightInit.XAVIER)  // Initializes the weights using Xavier initialization.
                        .build())
                .build();
        return conf;
    }

    /**
     * Create the Multi Layer Network Model and Run the Model
     * @param conf
     * @param mnistTrain
     * @param scoreIterations
     * @return
     */
    private static MultiLayerNetwork createAndRunMultiLayerNetworkModel(
            MultiLayerConfiguration conf, DataSetIterator mnistTrain, int scoreIterations) {
        Std.println("Step 3: Create the Multi Layer Network Model...  Iterations = "+scoreIterations);
        int numEpochs = 15; // number of epochs to perform
        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        // Print the score with every (100) iteration
        model.setListeners(new ScoreIterationListener(scoreIterations));
        Std.println("Step 4: Train the mode for Epochs =  "+numEpochs);
        model.fit(mnistTrain, numEpochs);
        return model;
    }

    /**
     * Evaluate the Model
     *
     * @param model
     * @param mnistTest
     */
    private static void evaluateModel(MultiLayerNetwork model, DataSetIterator mnistTest) {
        Std.println("Step 5: Evaluate the Model .... ");
        Evaluation eval = model.evaluate(mnistTest);
        Std.println("Step 5: Results:\n"+eval.stats());
        Std.println("Training Process Completed...... >>>>>>> ------------------------------");
    }
}
