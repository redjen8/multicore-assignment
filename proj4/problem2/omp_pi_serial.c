#include <omp.h>
#include <stdio.h>

long num_steps = 1000000000; 
double step;

void main ()
{ 
	long i; double x, pi, sum = 0.0;
	step = 1.0/(double) num_steps;
    double startTime, endTime;
    startTime = omp_get_wtime();
	#pragma omp parallel for reduction(+:sum) private(x) 
	for (i=0;i< num_steps; i++){
		x = (i+0.5)*step;
		sum = sum + 4.0/(1.0+x*x);
	}
	pi = step * sum;
    endTime = omp_get_wtime();
	printf("pi=%.24lf\n",pi);
    double timeDiff = endTime - startTime;
    printf("Execution Time : %lfms\n", timeDiff * 1000);
}