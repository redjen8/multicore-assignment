#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <cuda.h>

#define SPHERES 20

#define rnd( x ) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048

struct Sphere {
    float   r,b,g;
    float   radius;
    float   x,y,z;
    __device__ float hit( float ox, float oy, float *n ) {
        float dx = ox - x;
        float dy = oy - y;
        if (dx*dx + dy*dy < radius*radius) {
            float dz = sqrtf( radius*radius - dx*dx - dy*dy );
            *n = dz / sqrtf( radius * radius );
            return dz + z;
        }
        return -INF;
    }
};


__global__ void kernel_compute(Sphere* s, unsigned char* ptr)
{
	int tx = threadIdx.x + blockIdx.x * blockDim.x;
	int ty = threadIdx.y + blockIdx.y * blockDim.y;
	int offset = tx + ty*DIM;
	float ox = (tx - DIM/2);
	float oy = (ty - DIM/2);
	// printf("x:%d, y:%d, ox:%f, oy:%f\n",tx,ty,ox,oy);
	float r=0, g=0, b=0;
	float maxz = -INF;
	for(int i=0; i<SPHERES; i++) {
		float n;
		float   t = s[i].hit( ox, oy, &n );

		if (t > maxz) {
			float fscale = n;
			r = s[i].r * fscale;
			g = s[i].g * fscale;
			b = s[i].b * fscale;
			maxz = t;
		} 
	}

	ptr[offset*4 + 0] = (int)(r * 255);
	ptr[offset*4 + 1] = (int)(g * 255);
	ptr[offset*4 + 2] = (int)(b * 255);
	ptr[offset*4 + 3] = 255;
}

void ppm_write(unsigned char* bitmap, int xdim,int ydim, FILE* fp)
{
	int i,x,y;
	fprintf(fp,"P3\n");
	fprintf(fp,"%d %d\n",xdim, ydim);
	fprintf(fp,"255\n");
	for (y=0;y<ydim;y++) {
		for (x=0;x<xdim;x++) {
			i=x+y*xdim;
			fprintf(fp,"%d %d %d ",bitmap[4*i],bitmap[4*i+1],bitmap[4*i+2]);
		}
		fprintf(fp,"\n");
	}
}

int main (void) {
	unsigned char* bitmap_h;

	srand(time(NULL));

	FILE* fp = fopen("result_cuda.ppm","w");

    clock_t begin = clock();
    // allocate memory for host
	Sphere *sphere_h = (Sphere*)malloc( sizeof(Sphere) * SPHERES );
	for (int i=0; i<SPHERES; i++) {
		sphere_h[i].r = rnd( 1.0f );
		sphere_h[i].g = rnd( 1.0f );
		sphere_h[i].b = rnd( 1.0f );
		sphere_h[i].x = rnd( 2000.0f ) - 1000;
		sphere_h[i].y = rnd( 2000.0f ) - 1000;
		sphere_h[i].z = rnd( 2000.0f ) - 1000;
		sphere_h[i].radius = rnd( 200.0f ) + 40;
	}
	bitmap_h=(unsigned char*)malloc(sizeof(unsigned char)*DIM*DIM*4);

    //allocate memory for device
    int sphereSize = sizeof(Sphere) * SPHERES;
    int bitmapSize = sizeof(unsigned char) * DIM * DIM * 4;
    Sphere* sphere_d = (Sphere*) malloc (sphereSize);
    unsigned char* bitmap_d = (unsigned char*) malloc (bitmapSize);
    cudaMalloc((void**)&sphere_d, sphereSize);
    cudaMalloc((void**)&bitmap_d, bitmapSize);

    cudaMemcpy(sphere_d, sphere_h, sphereSize, cudaMemcpyHostToDevice);
    cudaMemcpy(bitmap_d, bitmap_h, bitmapSize, cudaMemcpyHostToDevice);
	dim3 dimBlock(32, 32);
	dim3 dimGrid(64, 64);
    kernel_compute<<<dimGrid, dimBlock>>>(sphere_d, bitmap_d);
	cudaDeviceSynchronize();

	cudaMemcpy(bitmap_h, bitmap_d, bitmapSize, cudaMemcpyDeviceToHost);

	cudaFree(sphere_d);
	cudaFree(bitmap_d);

	ppm_write(bitmap_h,DIM,DIM,fp);
    clock_t end = clock();
    double timeDiff = (double) (end-begin) / CLOCKS_PER_SEC;
	printf("Execution time : %lfms\n", timeDiff * 1000);
	printf("[%s] was generated.\n", "result_cuda.ppm");
	fclose(fp);
	free(bitmap_h);
	free(sphere_h);
	return 0;
}