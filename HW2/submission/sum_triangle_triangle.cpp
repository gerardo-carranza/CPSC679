//! \file examples/Minkowski_sum_2/sum_triangle_square.cpp
// Computing the Minkowski sum of a triangle and a square.
#include <typeinfo>
#include <iostream>
#include <fstream>
#include <CGAL/basic.h>
#include <CGAL/minkowski_sum_2.h>
#include <string>
#include "bops_linear.h"
using namespace std;
typedef Polygon_2::Vertex_iterator VertexIterator;
int main(int argc, char *argv[])
{
  VertexIterator start,end;
  Polygon_2   P;
  Polygon_2   Q;
  int x1,y1,x2,y2,x3,y3,a1,b1,a2,b2,a3,b3;
  FILE * myfile;
	string str = argv[1];
 
  
  std::ofstream outputfile;
  char out[strlen(argv[1])+3] = "ms_";
  strcat(out,argv[1]);
  outputfile.open(out);

  //ifstream myfile(str.c_str()); //c_str returns a pointer to an array http://www.cplusplus.com/reference/string/string/c_str/
	//myfile.open(str); // Either one of these can be used
  //myfile.open(str.c_str());
  myfile = fopen(str.c_str(),"r");
  while(fscanf(myfile,"%d %d %d %d %d %d %d %d %d %d %d %d",&x1,&y1,&x2,&y2,&x3,&y3,&a1,&b1,&a2,&b2,&a3,&b3)!= EOF)
  {
  //First triangle
  
  P.push_back(Point_2(x1, y1));  P.push_back(Point_2(x2, y2));
  P.push_back(Point_2(x3, y3));
  //std::cout << "P = " << P << std::endl;

  //Second triangle
  
  Q.push_back(Point_2(a1, b1));  Q.push_back(Point_2(a2, b2));
  Q.push_back(Point_2(a3, b3));
  
  //std::cout << "Q = " << Q << std::endl;

  //std::cout << "hi" <<std::endl;
  // Compute the Minkowski sum.
  Polygon_with_holes_2  sum = CGAL::minkowski_sum_2(P, Q);
  CGAL_assertion(sum.number_of_holes() == 0);
  //std::cout << typeid(sum.outer_boundary()).name() << '\n';
  //std::cout << "P (+) Q = " << sum.outer_boundary() << std::endl;
  VertexIterator start = sum.outer_boundary().vertices_begin(); // http://doc.cgal.org/latest/Polygon/classCGAL_1_1Polygon__2.html
  VertexIterator end = sum.outer_boundary().vertices_end();
    while(start != end){
    outputfile << *start << " " << std::flush;

    start++;
    }
    outputfile << "\n";

    //std:cout << *start << std::endl;
    //std::cout << "hi" <<std::endl;
  }
  return 0;
}
