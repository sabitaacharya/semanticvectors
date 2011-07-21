/**
   Copyright (c) 2007, University of Pittsburgh.

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

package pitt.search.semanticvectors;

import java.util.Random;
import java.util.logging.Logger;

import pitt.search.semanticvectors.vectors.Vector;
import pitt.search.semanticvectors.vectors.VectorFactory;
import pitt.search.semanticvectors.vectors.VectorUtils;

/**
 * Clusters search results using a simple k-means algorithm.
 * 
 * @author widdows@google.com (Your Name Here)
 */
public class ClusterResults {
  private static final Logger logger = Logger.getLogger(ClusterResults.class.getCanonicalName());
  
  /** 
   * Simple k-means clustering algorithm.
   * 
   * @param objectVectors Array of object vectors to be clustered.
   * @return Integer array parallel to objectVectors saying which
   * cluster each vector belongs to.
   */
  public static int[] kMeansCluster (ObjectVector[] objectVectors, int numClusters) {
    int[] clusterMappings = new int[objectVectors.length];
    Random rand = new Random();
    Vector[] centroids = new Vector[numClusters];

    logger.info("Initializing clusters ...");

    // Initialize cluster mappings randomly.
    for (int i = 0; i < objectVectors.length; ++i) {
      int randInt = rand.nextInt();
      while (randInt == Integer.MIN_VALUE) {
        //fix strange result where abs(MIN_VALUE) returns a negative number
        randInt = rand.nextInt();
      }
      clusterMappings[i] = Math.abs(randInt) % numClusters;
    }

    logger.info("Iterating k-means assignment ...");

    // Loop that computes centroids and reassigns members.
    while (true) {
      // Clear centroid register.
      for (int i = 0; i < centroids.length; ++i) {
        centroids[i] = VectorFactory.createZeroVector(Flags.vectortype, Flags.dimension); 
      }
      // Generate new cluster centroids.
      for (int i = 0; i < objectVectors.length; ++i) {
        centroids[clusterMappings[i]].superpose(objectVectors[i].getVector(), 1, null);
      }
      for (int i = 0; i < numClusters; ++i) {
        centroids[i].normalize();
      }

      boolean changeFlag = false;
      // Map items to clusters.
      for (int i = 0; i < objectVectors.length; i++) {
        int j = VectorUtils.getNearestVector(objectVectors[i].getVector(), centroids);
        if (j != clusterMappings[i]) {
          changeFlag = true;
          clusterMappings[i] = j;
        }
      }
      if (changeFlag == false) {
        break;
      }
    }

    logger.info("Got to stable clusters ...");
    return clusterMappings;
  }

  /**
   * Prints the following usage message: <br>
   * <code>
   * ClusterResults class in package pitt.search.semanticvectors
   * <br>Usage: java pitt.search.semanticvectors.BuildIndex 
   * <br>-numsearchresults [number of search results] 
   * <br>-numclusters [number of clusters]
   * <br> &lt;SEARCH ARGS&gt;
   * where SEARCH ARGS is an expression passed to Search class.
   * </code>
   * @see Search#usage
   */
  public static void usage() {
    String usageMessage = "\nClusterResults class in package pitt.search.semanticvectors"
      + "\nUsage: java pitt.search.semanticvectors.ClusterResults"
      + "\n                        -numsearchresults [number of search results]" 
      + "\n                        -numclusters [number of clusters]"
      + "\n                        <SEARCH ARGS>"
      + "\nwhere SEARCH ARGS is an expression passed to Search class.";
    System.err.println(usageMessage);
  }

  /**
   * Main function gathers search results for a particular query,
   * performs clustering on the results, and prints out results.
   * @param args
   * @see ClusterResults#usage
   */
  public static void main (String[] args) throws IllegalArgumentException {
    args = Flags.parseCommandLineFlags(args);

    // Get search results, perform clustering, and print out results.		
    ObjectVector[] resultsVectors = Search.getSearchResultVectors(args, Flags.numsearchresults);
    int[] clusterMappings = kMeansCluster(resultsVectors, Flags.numclusters);
    for (int i = 0; i < Flags.numclusters; ++i) {
      System.out.println("Cluster " + i);
      for (int j = 0; j < clusterMappings.length; ++j) {
        if (clusterMappings[j] == i) {
          System.out.print(resultsVectors[j].getObject() + " ");
        }
      }
      System.out.println();
    }
  }
}