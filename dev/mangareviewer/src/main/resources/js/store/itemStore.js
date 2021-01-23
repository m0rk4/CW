import itemApi from "api/itemApi"
import {extractNewTags} from "util/util";

export default {
    namespaced: true,
    state: () => ({
        collectionItems: []
    }),
    getters: {},
    mutations: {
        addNewItemMutation(state, savedItem) {
            state.collectionItems = [
                ...state.collectionItems,
                savedItem
            ]
        },
        setCollectionItemsMutation(state, newItems) {
            state.collectionItems = [
                ...newItems
            ]
        },
        updateItemMutation(state, updatedItem) {
            const index = state.collectionItems.findIndex(i => i.id === updatedItem.id)
            if (index > -1) {
                state.collectionItems = [
                    ...state.collectionItems.splice(0, index),
                    updatedItem,
                    ...state.collectionItems.splice(index + 1)
                ]
            }
        },
        deleteItemMutation(state, deletedItem) {
            const index = state.collectionItems.findIndex(i => i.id === deletedItem.id)
            if (index > -1) {
                state.collectionItems = [
                    ...state.collectionItems.splice(0, index),
                    ...state.collectionItems.splice(index + 1)
                ]
            }
        }
    },
    actions: {
        addNewItemAction({commit}, itemToAdd) {
            itemApi.addNewItem(itemToAdd).then(res => {
                res.json().then(savedItem => {
                    commit('addNewItemMutation', savedItem)
                    commit('tag/addTagsMutation',
                        extractNewTags(savedItem.tags, itemToAdd.tags), {root: true})
                })
            })
        },
        updateItemAction({commit}, itemToUpdate) {
            itemApi.updateItem(itemToUpdate).then(res => {
                res.json().then(updatedItem => {
                    commit('updateItemMutation', updatedItem)
                })
            })
        },
        deleteItemAction({commit}, itemToDelete) {
            itemApi.deleteItem(itemToDelete.id).then(res => {
                if (res.ok) {
                    commit('deleteItemMutation', itemToDelete)
                }
            })
        }
    }
}