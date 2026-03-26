# Event-Driven Agents System with Multi-threaded HTTP Server (Java)

## 📌 Overview
This project implements an event-driven system of agents communicating through a Publish-Subscribe architecture.

The system simulates distributed data flow between components and includes a custom multi-threaded HTTP server.

## 🚀 Key Features
- Event-driven architecture (Publish-Subscribe model)
- Multi-threaded HTTP server handling client requests
- Custom HTTP request parsing (headers, parameters, body)
- Topic-based communication between agents
- Graph-based dependency modeling with cycle detection
- Singleton pattern for centralized topic management

## 🧠 System Architecture
The system is built around:
- **Agents** – process and transform data
- **Topics** – communication channels between agents
- **Topic Manager (Singleton)** – manages topics globally
- **HTTP Server** – handles external requests

## 🛠 Technologies
- Java
- Object-Oriented Programming (OOP)
- Multithreading & Concurrency
- Design Patterns (Singleton, Pub-Sub)
- Networking (HTTP protocol)

## 📂 Main Components
- Agent implementations (PlusAgent, IncAgent, BinOpAgent)
- Topic & TopicManager
- HTTP Server implementation
- RequestParser
- Graph & Node system

## 🎯 Purpose
Developed as part of a Computer Science course, focusing on system design, concurrency, and communication between components.
