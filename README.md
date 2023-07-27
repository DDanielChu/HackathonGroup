# HackathonGroup

An android app that checks if a fact is real or fake by checking it against the LIAR dataset.
If a fact is not similar to anything in the dataset, it will be sent to the community polls for
users to vote on. If enough users agree that a fact is real or fake, it will be archived
and added to our dataset.

We use a unique method to check whether a voter is honest and knowledgeable: Amongst the facts
that could not be checked using the dataset, "test questions" will also appear at random times.
These test questions are simply facts taken directly from our dataset. Since we already know the
verity of these questions, the voter will gain credits if they answer correctly. Conversely,
the voter will lose credits if they answer incorrectly. As voters gain more credits, they will
progress through the ranking system. When higher ranked users vote on polls, their votes will count
for multiple users. For instance, the highest rank allows the voter to vote for 3 users. Negative
credits will result in a temporary ban from community polls, to prevent trolls and unknowledgeable
voters from skewing the results. 

NOTE: This prototype was built in 24 hours for a Toronto AI hackathon. It is not a finished product,
although many of the features work. To run this without errors, please use Android Studio.
