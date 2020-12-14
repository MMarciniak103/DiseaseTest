# To build image run:
```python
docker build -t myimage .
```

# To create and run container type:
```python
docker run -d -p 80:80 -v $(pwd):/app myimage /start-reload.sh
```
