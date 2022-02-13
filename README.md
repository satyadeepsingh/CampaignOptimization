# CampaignOptimization

campaign optimization service

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://github.com/satyadeepsingh/CampaignOptimization)

## _Springboot APIs for campaign optimization_

This is written as microservice API for managing campaigns and optimizing them using apis which is a microservice.

Prerequisites:

- Java 11
- ✨Maven

## Features

- View all campaign groups (through a call to campaign group)
- View all campaigns for a campaign group (through a call to campaign)
- View latest optimisations for a campaign group (through a call to optimisation)
- View latest recommendations for an optimisation (through a call to recommendation)
- Apply / accept latest optimisation / recommendations to a campaign group / campaigns

## Tech

The example uses a number of tech as follows:

- [Java 11] - Backend
- [springboot framework] - from spring
- [maven] - build tool for backend

## Installation

[Java ](https://www.oracle.com/ae/java/technologies/javase-jdk11-downloads.html) v11+

Install the dependencies and devDependencies and start the server.

```sh 
git clone https://github.com/satyadeepsingh/CampaignOptimization.git
cd campaign-optimization-service
git checkout main
./mvnw spring-boot:run
```

The data migration will load the data from the sample campaigns.csv file.

For frontend environments...

## Plugins

| Plugin | Download link |
| ------ | ------ |
| Postman | [https://www.postman.com/downloads/][PlDb] |

## Testing

Open postman and import the collection provided `Optily.postman_collection.json` in postman.

Postman consists of following examples:

-
    1. GetAllCampaigns - get all the campaign group names

-
    2. GetCampaignsOfGroup - get details of the campaigns of a group.
-
    3. OptimizeCampaignsOfGroup - Optimize a particular campaign of a group
-
    4. GetCampaignRecommendations - get a campaign recommended budget
-
    5. OptimizeAllCampaignsOfGroup - optimize all campaigns in a group

-
    6. GetCampaignGroupoptimizations - view optimizations of the campaign group

-
    7. GetCampaignGroupRecommendations - view campaigns which are not optimized/recommendations are not applied.

## License

**Free Software, Hell Yeah!**

## Connect with me

|linkedin|: [https://linkedin.com/in/satyadeep-singh-1b8b9242]
   |Twitter|: [https://twitter.com/Isatyadeepsingh]
  

