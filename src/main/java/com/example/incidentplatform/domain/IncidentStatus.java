package com.example.incidentplatform.domain;


//New/Submitted: The incident has been logged but not yet reviewed.
//        Assigned/Acknowledged: The incident has been assigned to a team or individual for investigation.
//        Investigating/Analyzing: The root cause is being actively researched.
//Work in Progress (WIP): The incident is being actively worked on.
//        Monitoring/Fixing: A fix is implemented, and the team is verifying functionality.
//        Resolved / Solved: The issue has been fixed and verified by technical staff.
//Closed: The final state, indicating the incident is resolved and documented, with no further action required.

public enum IncidentStatus {
    OPEN, INVESTIGATING, MONITORING, RESOLVED
}
