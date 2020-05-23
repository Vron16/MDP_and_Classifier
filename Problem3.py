import numpy as np
import heapq
from math import sqrt 

def euclid_distance(training_data_point, test_data_point):
	# Vectors must be the same size.
	euclid_distance = 0
	for i in range(len(training_data_point)):
		euclid_distance += (training_data_point[i] - test_data_point[i])**2

	euclid_distance = sqrt(euclid_distance)
	return euclid_distance

def knn(training_input, training_output, testing_input, k):
	# Iterate through each data point in testing_input
	num_test_points = testing_input.shape[0]
	num_training_points = training_input.shape[0]
	classifications = np.zeros(num_test_points)
	for i in range(num_test_points):
		# Create a max heap of k tuples, where tuple[0] is its Euclidean distance from the test input being processed and tuple[1] is its index.
		test_data_point = testing_input[i]
		#print('Finding nearest neighbors for', test_data_point)
		nearest_neighbors = []
		# Iterate through each data point in training_input
		for j in range(num_training_points):
			training_data_point = training_input[j]
			#print('Determining similarity to', training_data_point)
			# Calculate Euclidean distance between training and testing data point. Multiply by negative so that heap will be max heap.
			distance = euclid_distance(training_data_point, test_data_point)*-1
			#print('Distance is', distance)
			heapq.heappush(nearest_neighbors, (distance, j))
			# If the size of nearest_neighbors is greater than k, pop the tuple at the top.
			if (len(nearest_neighbors) > k):
				heapq.heappop(nearest_neighbors)

		# Now, we have the k nearest neighbors. Pop them out one at a time, and use the index to get the output value. Update the appropriate counter.
		# In case of a tie, store the index of the closest neighbor and use that instead.
		num_class_a = 0
		num_class_b = 0
		closest_neighbor_index = 0
		while (len(nearest_neighbors) > 0):
			index = heapq.heappop(nearest_neighbors)[1]
			if (len(nearest_neighbors) == 0):
				closest_neighbor_index = index
			image_class = training_output[index]
			if (image_class == 0):
				num_class_a += 1
			else:
				num_class_b += 1
		# Take the majority vote, breaking tie with closest_neighbor_index.
		#print('Number of similar neighbors classified as A', num_class_a)
		#print('Number of similar neighbors classified as B', num_class_b)
		if (num_class_a > num_class_b):
			classifications[i] = 0
		elif (num_class_a < num_class_b):
			classifications[i] = 1
		else:
			classifications[i] = training_output[closest_neighbor_index]

	print('Classifying test data with a K-Nearest Neighbors Classifier')
	for i in range(len(classifications)):
		print('Image', i)
		if (classifications[i] == 1):
			print('Predicted:', 'B')
		else:
			print('Predicted:', 'A')
		print('')

	


def make_prediction(input_data, weights):
	# First, dot the weights with the input data.
	dot_product = np.dot(input_data, weights)
	# Predict 1 if dot_product is greater than or equal to 0
	prediction = 0
	if (dot_product) >= 0:
		prediction = 1

	return prediction


def train(training_input, training_output, weights):
	# First-initialize num_misclassified to 1 and prepend 1 to the start of each row of inputs.
	num_misclassified = 1
	training_input_bias = np.insert(training_input, 0, 1, axis=1)
	# We want to train until the entire training dataset is classified perfectly.
	while (num_misclassified > 0):
		num_misclassified = 0
		num_data_points = training_input_bias.shape[0]
		num_features = training_input_bias.shape[1]
		
		for i in range(num_data_points):
			#keep_going = input("Press c to proceed")
			x_i = training_input_bias[i] # Get the input data point vector with bias=1 prepended.
			#print("Data point ", i, "Input:", x_i) 
			y_i = training_output[i] # Get the output data point.
			#print("Data point ", i, "Output:", y_i) 	
			#print("Data point ", i, "Weights:", weights)

			# Now, call make_prediction to make a prediction based on the input data vector and the weights.
			prediction = make_prediction(x_i, weights)
			
			# If the absolute value of the difference is > 0, then there was a misclassification.
			if (abs(prediction - y_i) > 0):
				num_misclassified += 1
			# Correction represents the vector formed by multiplying the difference in predicted
			# and actual with x_i. If there's no difference, correction is a zero vector. If prediction
			# was 0 and the actual is 1, then we end up adding the input vector to the weights. Otherwise,
			# we end up subtracting the input vector from the weights.
			correction = (prediction - y_i) * x_i
			weights = weights - correction

		#print('Number of misclassified points: ', num_misclassified)
		
	return weights


def perceptron(training_input, training_output, testing_input):
	# First, let's initialize the weights vector. It's exactly 1 element longer than the input arrays due to the bias weight.
	# All weights start at 0.
	weights = np.zeros(26)
	# Then, call the train function, defined above, to train the data.
	new_weights = train(training_input, training_output, weights)
	# Print the trained weights and verify that the trained set of weights classifies the training data perfectly.
	print('Trained Weights: ', new_weights)
	print('Testing training dataset to guarantee perfect classification...')
	training_input_bias = np.insert(training_input, 0, 1, axis=1)
	num_data_points = training_input_bias.shape[0]
	
	for i in range(num_data_points):
		prediction = make_prediction(training_input_bias[i], new_weights)
		print('Image', i)
		if (prediction == 1):
			print('Predicted:', 'B')
		else:
			print('Predicted:', 'A')
		if (training_output[i] == 1):
			print('Actual:', 'B')
		else:
			print('Actual:', 'A')
		print('')


	# Now, classify the test data!
	print('Classifying test data images based on the trained perceptron.')
	testing_input_bias = np.insert(testing_input, 0, 1, axis=1)
	num_data_points = testing_input_bias.shape[0]
	for i in range(num_data_points):
		prediction = make_prediction(testing_input_bias[i], new_weights)
		print('Image', i)
		if (prediction == 1):
			print('Predicted:', 'B')
		else:
			print('Predicted:', 'A')
		print('')

def main():
	# Hard-coding the input vectors for this problem by image.
	training_input = np.array([[0,0,1,0,0,0,1,0,1,0,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1],
							  [1,1,1,0,0,1,0,0,1,0,1,1,1,0,0,1,0,0,1,0,1,1,1,0,0],
							  [0,1,1,0,0,1,0,0,1,0,1,0,0,0,0,1,0,0,1,0,0,1,1,0,0],
							  [1,1,1,0,0,1,0,0,1,0,1,0,0,1,0,1,0,0,1,0,1,1,1,0,0],
							  [1,1,1,1,0,1,0,0,0,0,1,1,1,1,0,1,0,0,0,0,1,1,1,1,0],
							  [1,1,1,1,0,1,0,0,0,0,1,1,1,0,0,1,0,0,0,0,1,0,0,0,0],
							  [1,1,1,1,0,1,0,0,1,0,1,0,0,0,0,1,0,1,1,1,1,1,1,1,0],
							  [1,0,0,0,1,1,0,0,0,1,1,1,1,1,1,1,0,0,0,1,1,0,0,0,1],
							  [1,1,1,1,1,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,1,1,1,1,1],
							  [1,1,1,1,0,0,1,0,0,0,0,1,0,0,0,0,1,0,1,0,0,1,1,1,0]])
	# Class A is 0, class B is 1.
	training_output = np.array([0,0,0,0,0,1,1,1,1,1])

	testing_input = np.array([[1,0,0,1,0,1,0,1,0,0,1,1,0,0,0,1,0,1,0,0,1,0,0,1,0],
							 [1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,1,1,1,0],
							 [1,1,0,1,1,1,0,1,0,1,1,0,1,0,1,1,0,0,0,1,1,0,0,0,1],
							 [1,1,0,0,1,1,1,1,0,1,1,0,1,1,1,1,0,0,1,1,1,0,0,0,1],
							 [0,1,1,1,0,1,0,0,0,1,1,0,0,0,1,1,0,0,0,1,0,1,1,1,0]])
	print('Model 1: Perceptron Learning Algorithm')
	perceptron(training_input, training_output, testing_input)
	print('Model 2: K-Nearest Neighbors Classifier')
	for k in range(1,7):
		print('KNN with k =', k)
		knn(training_input, training_output, testing_input, k)



main()