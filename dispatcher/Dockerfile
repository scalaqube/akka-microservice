FROM golang:1.6.1-alpine

EXPOSE 9001

CMD ["./dispatcher"]

COPY dispatcher.go .
RUN go build dispatcher.go
