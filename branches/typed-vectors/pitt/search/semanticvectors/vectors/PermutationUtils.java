/**
   Copyright (c) 2011, the SemanticVectors AUTHORS.

   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are
   met:

 * Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above
   copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided
   with the distribution.

 * Neither the name of the University of Pittsburgh nor the names
   of its contributors may be used to endorse or promote products
   derived from this software without specific prior written
   permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
   A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 **/

package pitt.search.semanticvectors.vectors;

/**
 * Class that provides utilities for generating special permutations.
 *
 * Permutations themselves are just represented as int[] arrays saying.  It is presumed that
 * each such array is a permutation of the numbers from 1 to n, where n is the length of the
 * array.  This invariant could be enforced by making a Permutation class but this has not been
 * considered necessary to date.
 * 
 * @author widdows
 */
public class PermutationUtils {

  private PermutationUtils() {}

  public static int[] getShiftPermutation(int dimension, int shift) {
    int[] permutation = new int[dimension];

    for (int i = 0; i < dimension; ++i) {
      int entry = (i + shift) % dimension;
      if (entry < 0) entry += dimension;
      permutation[i] = entry;
    }
    return permutation;
  }

  public static int[] getInversePermutation(int[] permutation) {
    int[] inversePermutation = new int[permutation.length];
    for (int x=0; x < permutation.length; x++) {
      inversePermutation[permutation[x]] = x;
    }
    return inversePermutation;  
  }
}
