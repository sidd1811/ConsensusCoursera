package com.coursera.consensus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {

	private Set<Transaction> pendingTransactions;

	public Set<Integer> followees = new HashSet<Integer>();

	public double p_graph;
	public double p_malicious;
	public double p_txDistribution; 
	public int numRounds; 
	public int numRoundsTemp;

	public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
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
		Set<Transaction> toSend = new HashSet<>(pendingTransactions);
		pendingTransactions.clear();
		return toSend;
	}

	public void receiveFromFollowees(Set<Candidate> candidates) {

		HashMap<Integer, Set<Transaction>> senderTxMap = new HashMap<Integer, Set<Transaction>>();

		System.out.println("Round#: " + numRoundsTemp);

		Iterator<Integer> iter = this.followees.iterator();
		while(iter.hasNext()) {
			System.out.print( iter.next() + ", ");
		}
		System.out.println();

		for(Candidate candidate : candidates) {
			if(this.followees.contains(candidate.sender)) {
				if(senderTxMap.get(candidate.sender) != null)
					senderTxMap.get(candidate.sender).add(candidate.tx);
				else {
					Set<Transaction> txSet = new HashSet<>();
					senderTxMap.put(candidate.sender, txSet);
				}
			}
		}


		this.numRoundsTemp--;
		for(Integer sender : senderTxMap.keySet()) {
			this.pendingTransactions.addAll(senderTxMap.get(sender));
		}

		Set<Integer> tempFollowees = new HashSet<Integer>(this.followees);
		tempFollowees.removeAll(senderTxMap.keySet());
		this.followees.removeAll(tempFollowees);
	}
}
