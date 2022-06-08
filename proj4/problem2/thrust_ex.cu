#include <iostream>
#include <cuda.h>
#include <cmath>

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

double step;

__global__ void kernel_compute() {
    printf("Hello, world!\n");
}

__shared__ int sum;

int main(void) {
    step = 1.0 / (double)N;
    dim3 dimBlock(32, 32);
    dim3 dimGrid(1024, 1024);
    kernel_compute<<<dimGrid, dimBlock>>>();
    gpuErrchk(cudaDeviceSynchronize());
    return 0;
}