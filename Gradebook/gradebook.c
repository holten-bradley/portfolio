#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "gradebook.h"

/*
Code edited by Holten Bradley (bradl515)
    *With help from multiple TA's and Prof. Kolb*
10/05/21
*/

gradebook_t *create_gradebook(const char *class_name) {
    gradebook_t *new_book = malloc(sizeof(gradebook_t));
    if (new_book == NULL) {
        return NULL;
    }

    strcpy(new_book->class_name, class_name);
    new_book->root = NULL;
    return new_book;
}//Credit: creator of gradebook.c

const char *get_gradebook_name(const gradebook_t *book) {
    return book->class_name;
}

int add_score(gradebook_t *book, const char *name, int score) {
    if (book->root == NULL && score >=0) {                  //nothing at the root? add it!
        node_t *newNode = malloc(sizeof(node_t));

        strcpy(newNode->name, name);
        newNode->score = score;
        newNode->left = NULL;
        newNode->right = NULL;

        book->root = newNode;
        return 0;

    } else if (score >=0) {
       
        node_t *newNode = book->root;

        while (newNode != NULL){
            if (strcmp(newNode->name, name) == 0){          //we found the name: now update the score
                newNode->score = score;
                return 0;
            }else if(strcmp(newNode->name, name) < 0){      //have got to look left
                if (newNode->left == NULL) {                //nothing left? add it!
                    node_t *temp = malloc(sizeof(node_t));

                    strcpy(temp->name, name);
                    temp->score = score;
                    temp->left = NULL;
                    temp->right = NULL;

                    newNode->left = temp;
                    return 0;
                }else {                                     //something left? keep looking
                    newNode = newNode->left;
                }
            }else{
                if (newNode->right == NULL) {               //nothing right? add it!
                    node_t *temp = malloc(sizeof(node_t));

                    strcpy(temp->name, name);
                    temp->score = score;
                    temp->left = NULL;
                    temp->right = NULL;

                    newNode->right = temp;
                    return 0;
                }else {                                     //something right? keep looking
                    newNode = newNode->right;
                }
            }
        }//while

    }//else if

    return -1;
}


int find_score(const gradebook_t *book, const char *name) {
    node_t *searchNode = book->root;

    while (searchNode != NULL){
        if (strcmp(searchNode->name, name) == 0){
            return searchNode->score;
        }else if(strcmp(searchNode->name, name) < 0){
            searchNode = searchNode->left;
        }else{
            searchNode = searchNode->right;
        }
    }

    return -1;
}

//My print helper: prints all the nodes
void print_nodes(node_t *nodes) {
    if(nodes == NULL){
        return;
    }else{
        print_nodes(nodes->right);
        printf("%s: %d\n", nodes->name, nodes->score);
        print_nodes(nodes->left);
    }
}

void print_gradebook(const gradebook_t *book) {
    printf("Scores for all students in %s:\n", book->class_name);
    if (book->root != NULL) {
        print_nodes(book->root);
    }
}

//My node helper: finds all the nodes
void free_nodes(node_t *current) {
    if(current == NULL) {
        return;
    } else {
        free_nodes(current->left);
        free_nodes(current->right);
        free(current);
    }
}

void free_gradebook(gradebook_t *book) {
    if (book != NULL) {
        if (book->root != NULL) {
        free_nodes(book->root);
        } 
        free(book);  
    }
}

// Helper function to allow recursion for writing out tree
int write_gradebook_to_text_aux(const node_t *current, FILE *f) {
    if (current == NULL) {
        return 0;
    }

    // Each line is formatted as "<name> <score>"
    fprintf(f, "%s %d\n", current->name, current->score);

    if (current->left != NULL) {
        if (write_gradebook_to_text_aux(current->left, f) != 0) {
            return -1;
        }
    }

    if (current->right != NULL) {
        if (write_gradebook_to_text_aux(current->right, f) != 0) {
            return -1;
        }
    }

    return 0;
}//Credit: creator of gradebook.c

int write_gradebook_to_text(const gradebook_t *book) {
    char file_name[MAX_NAME_LEN + strlen(".txt")];
    strcpy(file_name, book->class_name);
    strcat(file_name, ".txt");

    FILE *f = fopen(file_name, "w");
    if (f == NULL) {
        return -1;
    }

    int result = write_gradebook_to_text_aux(book->root, f);
    fclose(f);
    return result;
}//Credit: creator of gradebook.c

gradebook_t *read_gradebook_from_text(const char *file_name) {
    
    FILE *file_handle = fopen(file_name, "r");
    if (file_handle == NULL) {
        return NULL;
    }
    
    int index = strlen(file_name);
    char class[index-3];
    strncpy(class, file_name, (index-4));
    class[index-4] = '\0';
    gradebook_t *book_copy = create_gradebook(class);
    
    while(!feof(file_handle)){
        int score;
        char name[MAX_NAME_LEN];
        
        if(fscanf(file_handle, "%s %d", name, &score) == 2) {
            add_score(book_copy, name, score);
        } 
    }

    fclose(file_handle);
    return book_copy;
    
}

//My helper for binary
int write_gradebook_to_binary_aux(node_t *current, FILE *file_handle){
    if(current == NULL) {
        return 0;
    }
    
    int size = strlen(current->name) + 1;
    fwrite(&size, sizeof(int), 1, file_handle);
    fwrite(current->name, sizeof(char), size, file_handle);
    fwrite(&current->score, sizeof(int), 1, file_handle);
    

    if (current->left != NULL) {
        if (write_gradebook_to_binary_aux(current->left, file_handle)){
            return -1;
        }
    }

    if (current->right != NULL) {
        if (write_gradebook_to_binary_aux(current->right, file_handle)){
            return -1;
        }
    }
    
    return 0;
}

int write_gradebook_to_binary(const gradebook_t *book) {
    // TODO: Not yet working
    char file_name[MAX_NAME_LEN + strlen(".bin")];
    strcpy(file_name, book->class_name);
    strcat(file_name, ".bin");

    FILE *file_handle = fopen(file_name, "w");
    if (file_handle == NULL) {
        return -1;
    }
    
    int result = write_gradebook_to_binary_aux(book->root, file_handle);
    
    fclose(file_handle);
    return result;
}

gradebook_t *read_gradebook_from_binary(const char *file_name) {
    // TODO: Not yet implemented
    FILE *file_handle = fopen(file_name, "r");
    if (file_handle == NULL) {
        return NULL;
    }

    int index = strlen(file_name);
    char class[index-3];
    strncpy(class, file_name, (index-4));
    class[index-4] = '\0';
    gradebook_t *book_copy = create_gradebook(class);

    while(!feof(file_handle)){
        int score;
        int length;
        char name[MAX_NAME_LEN];

        fread(&length, sizeof(int), 1, file_handle);
        fread(name, sizeof(char), length, file_handle);
        fread(&score, sizeof(int), 1, file_handle);
        add_score(book_copy, name, score);
    }

    fclose(file_handle);
    return book_copy;
    return 0;
}

/*
Have mercy. I got sent in many office hours.
*/
