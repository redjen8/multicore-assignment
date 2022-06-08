#include <iostream>
#include <cuda.h>
#include <cmath>
#include <chrono>
#include <thrust/transform_reduce.h>
#include <thrust/device_vector.h>
#include <thrust/sequence.h>

#define N 1000000000

struct convert_pi
{
	const double step = 0.000000001;
	__device__ 
	double operator() (const int& x) const {
		return 4.0 / (1.0 + ((x + 0.5) * step) * ((x + 0.5) * step));
	}
};

int main(void) {
	double alpha = 1 / (double) N;

	auto startTime = std::chrono::high_resolution_clock::now();
	thrust::device_vector<int> data(N);
	thrust::sequence(data.begin(), data.end());
	convert_pi unary_op;
	thrust::plus<double> binary_op;
	double result = thrust::transform_reduce(thrust::device, data.begin(), data.end(), unary_op, (double) 0.0, binary_op) * alpha;
	cudaDeviceSynchronize();
	auto endTime = std::chrono::high_resolution_clock::now();
	auto timeDiff = std::chrono::duration_cast<std::chrono::microseconds>(endTime - startTime);
	std::cout.precision(24);
	std::cout << "PI : " << result << std::endl;
	std::cout << "Execution time : " << timeDiff.count() / 1000 << " ms" << std::endl;
	return 0;
}