#include <iostream>
#include <cuda.h>
#include <cmath>
#include <thrust/fill.h>
#include <thrust/device_vector.h>

#define N 1000000000

#define gpuErrchk(ans) { gpuAssert((ans), __FILE__, __LINE__); }
inline void gpuAssert(cudaError_t code, char *file, int line, bool abort=true)
{
   if (code != cudaSuccess)
   {
      fprintf(stderr,"GPUassert: %s %s %d\n", cudaGetErrorString(code), file, line);
      if (abort) exit(code);
   }
}

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
	thrust::device_vector<int> data(N);
	thrust::sequence(data.begin(), data.end());
	// for (int i = 0; i<data.size(); i++) {
	// 	std::cout << "data [" << i << "] = " << data[i] << std::endl;
	// }
	convert_pi unary_op;
	thrust::plus<double> binary_op;
	double result = thrust::transform_reduce(thrust::device, data.begin(), data.end(), unary_op, (double) 0.0, binary_op) * alpha;
	gpuErrchk(cudaDeviceSynchronize());
	// std::cout << alpha << std::endl;
	std::cout.precision(24);
	std::cout << "PI : " << result << std::endl;
	return 0;
}