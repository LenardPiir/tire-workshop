Requirements: Docker
Running the application locally: Run: docker-compose build docker-compose up. Project will open on localhost:80
Project structure: Project consists of frontend, backend(workshop) and nginx(for proxying requests between frontend and backend).
Backend arhitecture is made to handle as much different workshops as needed. Workshops are added by package and there is a collector service that 
collects all workshops together. Currently there are 2 workshops (London and Manchester).

For further development: 
1) Read workshop data into application by a file. Workshop data should be changable via metadata.
2) Add integration tests to test external services and collector service
3) Add customer session
4) Handle edge case where two users are booking available time at the same time
