#include <stdio.h>
#include <cuda.h>
#include <math.h>

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

__global__ void kernel_compute() {
    printf("Hello, world!\n");
}

int main(void) {
    kernel_compute<<<10, 1>>>();
    return 0;
}