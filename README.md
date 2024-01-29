# Disease Spread Simulation

This project simulates the spread of a disease using agent-based modeling (ABM) with Repast Simphony. It includes both traditional fully mixed SI and SIR models (Part A) and social network-based SI and SIR models (Part B).

## Table of Contents

- [Introduction](#introduction)
- [Part A: Traditional Fully Mixed Modeling](#part-a-traditional-fully-mixed-modeling)
  - [SI Model](#si-model)
  - [SIR Models](#sir-models)
- [Part B: Social Network-based SI and SIR Models](#part-b-social-network-based-si-and-sir-models)
  - [Barabasi-Albert SI Model](#barabasi-albert-si-model)
  - [Barabasi-Albert SIR Model](#barabasi-albert-sir-model)

## Introduction

This project aims to visually represent the spread of a disease within a population using ABM. Two modeling approaches are explored: traditional fully mixed modeling and social network-based modeling. The focus is on analyzing the changes in the number of susceptible, infected, and recovered individuals over time for different scenarios.

## Part A: Traditional Fully Mixed Modeling

In this part, the simulation assumes that each agent has the potential to infect all susceptible individuals in the group. The primary goal is to trace changes in the number of susceptible, infected, and recovered individuals over time. Different values of β and γ model parameters are explored.

### SI Model

- **β = 0.5**
- **N = 750**

![SI Model](Images/SI.png)

### SIR Models

#### β = 0.5, γ = 0.3, N = 750

![SIR Model](Images/SIR.png)

#### β = 0.9, γ = 0.1, N = 750

![SIR Model](Images/SIR_High_Infection_Low_Recovery.png)

#### β = 0.3, γ = 0.8, N = 750

![SIR Model](Images/SIR_Low_Infection_High_Recovery.png)

## Part B: Social Network-based SI and SIR Models

Recognizing the limitation of fully mixed models, Part B introduces a synthetic social network to model realistic interactions. Agents can only infect individuals they are directly connected to on the contact network. Synthetic networks are generated using Small World and Barabasi-Albert models.

### Barabasi-Albert SI Model

- **β = 0.2**
- **N = 750**

![Barabasi-Albert SI Model](Images/Barabasi_SI.png)

### Barabasi-Albert SIR Model

- **β = 0.8, γ = 0.3, N = 750**

![Barabasi-Albert SIR Model](Images/Barabasi_SIR.png)
