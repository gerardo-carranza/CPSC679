#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

int checkForIntersection(int x, int y, double **ptArray, double *n, double *average);
double *slopeLine(double *a, double *b);
double dotProduct(double *a, double *b);
double *computeAverage(double **ptArray, int size);
int main(int argc, char const *argv[])
{
	int m;
	int errorcount=0;
	double *n;
	double *average;
	int size = 5;
	double **ptArray;
	int i=0;
	int j=0;
	int c;
	ptArray = malloc(sizeof(double *)*size);
	assert(ptArray);

	while (i<5) {
		if (j == 0) {
			ptArray[i] = malloc(sizeof(double) * 3);
		}
		scanf("%d", &c);
		ptArray[i][j] = (double)c;
		j ++;
		if (j >= 3) {
			i ++;
			j = 0;
		}
	}

	average = malloc(sizeof(double)*3);
	
	average = computeAverage(ptArray, size);
	printf("Average= (%lf,%lf,%lf)\n",average[0],average[1],average[2]);
	

	
	//Read in directional vector n
	n = malloc(sizeof(int)*3);
	for(m=0; m<3; m++)
	{
		scanf("%d",&c);
		n[m]=(double)c;
	}
	printf("Vector n = %lf,%lf,%lf\n",n[0],n[1],n[2]);

	//Pts1-2
	errorcount += checkForIntersection(1,2,ptArray,n,average);
	//Pts2-3
	errorcount += checkForIntersection(2,3,ptArray,n,average);
	//Pts3-4
	errorcount += checkForIntersection(3,4,ptArray,n,average);
	//Pts4-5
	errorcount += checkForIntersection(4,5,ptArray,n,average);
	//Pts5-3
	errorcount += checkForIntersection(5,3,ptArray,n,average);
	//Pts 4-1
	errorcount += checkForIntersection(4,1,ptArray,n,average);
	//Pts 3-1
	errorcount += checkForIntersection(3,1,ptArray,n,average);
	
	if(errorcount==7){
		printf("Error");
	}
	return 0;

}

int checkForIntersection(int x, int y, double **ptArray, double *n,double *average){
	double a,b,c;
	double *l;
	// p0 - l0
	double *pl; 
	double scalar;
	//arrays, DUH
	x--;
	y--;
	l = malloc(sizeof(double)*3);
	//Pts 1-2
	l = slopeLine(ptArray[x],ptArray[y]);
	// if(x==2 && y==3)
	// {
	// 	printf(" Pt3 %lf\n",ptArray[3][2]);
	// 	printf("%lf",l[0]);
	// 	// assert(l[0]==0);
	// 	// assert(l[1]==1);
	// 	// assert(l[2]==0);		
	// 	//assert(dotProduct(n,l)==1);
	// }
	//printf(" Dot Product%lf\n",dotProduct(n,l));
	if(abs(dotProduct(n,l)) < 10e-5)
	{
		return 1;
	}
	else{
		pl[0] = average[0] - ptArray[x][0];
		pl[1] = average[1] - ptArray[x][1];
		pl[2] = average[2] - ptArray[x][2];
		scalar = dotProduct(pl,n)/dotProduct(l,n);
		a = scalar*l[0] + ptArray[x][0];
		b = scalar*l[1] + ptArray[x][1];
		c = scalar*l[2] + ptArray[x][2];
		
		printf("Intersection (pts %d-%d): (%lf,%lf,%lf)\n",x+1,y+1,a,b,c);
		return 0;
	}
}

double *slopeLine(double *a, double *b){
	double *l;
	l=malloc(sizeof(double)*3);
	l[0]= b[0]-a[0];
	l[1]= b[1]-a[1];
	l[2]= b[2]-a[2];
	return l;
}

double dotProduct(double *a, double *b)
{
	return a[0]*b[0] + a[1]*b[1] + a[2]*b[2];
}

double *computeAverage(double **ptArray, int size)
{
	double *averageArray;
	int i;
	int j;

	averageArray = malloc(sizeof(double)*3);
	assert(averageArray); 
	memset(averageArray,0,sizeof(double)*3);

	//compute sums
	for(j=0; j<3; j++)
	{
		for(i=0; i<size; i++)
		{
		averageArray[j]+= ptArray[i][j];
		}
	}

	//compute coordinate averages
	for(j=0; j<3; j++)
	{
		averageArray[j]=(double)averageArray[j]/size;
	}

	return averageArray;

}