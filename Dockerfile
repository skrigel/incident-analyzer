FROM ubuntu:latest
LABEL authors="sashakrigel"

ENTRYPOINT ["top", "-b"]