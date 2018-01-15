package com.coursera.consensus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class MaliciousNode implements Node {

	private Set<Transaction> pendingTransactions;

	public HashSet<Integer> followees = new HashSet<Integer>();
	public int numRoundsTemp;
	public double p_graph;
	public double p_malicious;
	public double p_txDistribution; 
	public int numRounds; 

	public MaliciousNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
		this.p_graph = p_graph;
		this.p_malicious = p_malicious;
		this.p_txDistribution = p_txDistribution;
		this.numRounds = numRounds;
		this.numRoundsTemp = numRounds;
	}

	public void setFollowees(boolean[] followees) {
		for(int i = 0; i < followees.length; i++) {
			if(followees[i]) {
				this.followees.add(i);
			}
		}
	}

	public void setPendingTransaction(Set<Transaction> pendingTransactions) {
		this.pendingTransactions = pendingTransactions;
	}

	public Set<Transaction> sendToFollowers() {
		Transaction tx = new Transaction(-1232);
		Transaction tx2 = new Transaction(-2222);
		Set<Transaction> txSet = new HashSet<>();
		txSet.add(tx2);
		txSet.add(tx);
		return txSet;
	}

	public void receiveFromFollowees(Set<Candidate> candidates) {
		HashMap<Integer, Set<Transaction>> senderTxMap = new HashMap<Integer, Set<Transaction>>();

		for(Candidate candidate : candidates) {
			if(this.followees.contains(candidate.sender)) {
				if(!(candidate.tx.equals(null))) {
					if(senderTxMap.get(candidate.sender) != null)
						senderTxMap.get(candidate.sender).add(candidate.tx);
					else {
						Set<Transaction> txSet = new HashSet<>();
						senderTxMap.put(candidate.sender, txSet);
					}
				}
			}
			else {}
			//				System.out.println("check");
		}

		if(this.numRoundsTemp != 0) {
			this.numRoundsTemp--;
			for(Integer sender : senderTxMap.keySet()) {
				if(!this.pendingTransactions.containsAll(senderTxMap.get(sender))) {
					this.pendingTransactions.addAll(senderTxMap.get(sender));
				}else {
					this.followees.remove(sender);
				}

			}
		}else {
			Iterator<Integer> iterKey = senderTxMap.keySet().iterator();
			this.numRoundsTemp = this.numRounds;
			this.pendingTransactions.clear();
			this.pendingTransactions.addAll(senderTxMap.get(iterKey.next()));
		}

	}
}
